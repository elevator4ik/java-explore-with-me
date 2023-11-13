package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.RequestStat;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServiceRepository extends JpaRepository<Stat, Long> {

    @Query("select new ru.practicum.model.RequestStat(stat.app, stat.uri, count(distinct stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between :start and :end " +
            "and (stat.uri in :uris OR :uris = null) " +
            "group by stat.app, stat.uri " +
            "order by count(stat.ip) desc")
    List<RequestStat> getAllStatistic(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.model.RequestStat(stat.app, stat.uri, count(stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between :start and :end " +
            "and (stat.uri in :uris OR :uris = null) " +
            "group by stat.app, stat.uri " +
            "order by count(stat.ip) desc")
    List<RequestStat> getAllStatisticNonUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}