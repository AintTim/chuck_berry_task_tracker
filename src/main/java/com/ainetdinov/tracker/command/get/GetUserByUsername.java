package com.ainetdinov.tracker.command.get;

import com.ainetdinov.tracker.command.Command;
import com.ainetdinov.tracker.model.dto.UserDto;
import com.ainetdinov.tracker.model.mapper.UserMapper;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class GetUserByUsername implements Command<UserDto> {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserRequest user;

    @Override
    public UserDto execute() {
        try (Session session = repository.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            var object = repository.findByUsername(session, user);
            transaction.commit();
            return object
                    .map(mapper::toDto)
                    .orElse(null);
        }
    }
}
