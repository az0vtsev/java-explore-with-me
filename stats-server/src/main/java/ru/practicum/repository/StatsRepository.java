package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Integer> {
    @Query(" select s from Stat s "
            + "where (s.timestamp >= ?1 and s.timestamp <= ?2) "
            + "and (s.uri IN(?3)) ")
    List<Stat> findStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(" select s from Stat s "
            + "where (s.timestamp >= ?1 and s.timestamp <= ?2) ")
    List<Stat> findStatsAllUri(LocalDateTime start, LocalDateTime end);

    @Query(" select count(s) from Stat s "
            + "where (s.timestamp >= ?1 and s.timestamp <= ?2) "
            + "and s.uri = ?3 ")
    Integer findStatsCount(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select distinct count(s.ip) from Stat s "
            + "where (s.timestamp >= ?1 and s.timestamp <= ?2) "
            + "and s.uri = ?3 ")
    Integer findStatsCountDistinct(LocalDateTime start, LocalDateTime end, String uri);

}
