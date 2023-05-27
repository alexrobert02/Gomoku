package com.example.DataBase.Repository;

import com.example.DataBase.DataBasePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<DataBasePlayer, Long> {
}