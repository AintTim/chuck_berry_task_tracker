package com.ainetdinov.tracker.command.validate;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class ValidateUserPresence implements Command<Boolean> {
    private final UserRepository userRepository;
    private final UserRequest userRequest;

    @Override
    public Boolean execute() {
        boolean result = false;
        try (Session session = userRepository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var user = userRepository.findByUsername(session, userRequest);
            if (user.isPresent()) {
                result = user.get().getPassword().equalsIgnoreCase(userRequest.getPassword());
            }
            transaction.commit();
        }
        return result;
    }
}
