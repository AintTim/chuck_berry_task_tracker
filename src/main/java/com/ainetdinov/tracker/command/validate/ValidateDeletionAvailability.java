package com.ainetdinov.tracker.command.validate;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.entity.Task;
import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@RequiredArgsConstructor
public class ValidateDeletionAvailability implements Command<Boolean> {
    private final LabelRepository repository;
    private final LabelRequest request;

    @Override
    public Boolean execute() {
        boolean isInvalid = true;
        try (Session session = repository.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            var label = repository.findById(session, request.getId());
            if (findTaskWithSingleLabel(session, label.orElseThrow()).getResultList().isEmpty()) {
                isInvalid = false;
            }
            transaction.commit();
        }
        return isInvalid;
    }

    private Query<Task> findTaskWithSingleLabel(Session session, Label label) {
        var q = """
                    select t
                    from Task t
                    where t.id in
                    (select t.id
                        from Task t
                        join t.labels ttl
                        group by t.id
                        having count(ttl.id) = 1
                                and sum(case when ttl.id = :label then 1 else 0 end) = 1)
                """;
        return session.createQuery(q, Task.class).setParameter("label", label.getId());
    }
}
