package com.example.prog4.repository.db1;

import com.example.prog4.repository.db1.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    @Query(value = "select * from session s where s.sessionId = :sessionId order by s.timeout desc limit 1", nativeQuery = true)
    Optional<Session> findOneBySessionId(@Param("sessionId") String sessionId);

    @Query(value = "select * from session s where s.sessionId = :sessionId", nativeQuery = true)
    List<Session> findAllBySessionId(@Param("sessionId") String sessionId);
}
