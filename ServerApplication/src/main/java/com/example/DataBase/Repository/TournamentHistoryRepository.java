package com.example.DataBase.Repository;

import com.example.DataBase.TournamentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentHistoryRepository extends JpaRepository<TournamentHistory, Long> {
}
