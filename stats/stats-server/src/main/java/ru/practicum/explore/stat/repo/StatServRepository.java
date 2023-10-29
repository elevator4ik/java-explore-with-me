package ru.practicum.explore.stat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.stat.module.RequestStat;
import ru.practicum.explore.stat.module.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServRepository extends JpaRepository<Stat, Integer> {

    @Query("select new ru.practicum.explore.stat.module.RequestStat(stat.app, stat.uri, count(distinct stat.ip)) " +
            "from Stat stat " +
            "where stat.dateTimeIncome between :start and :end " +
            "and (stat.uri in :uris OR :uris = null) " +
            "group by stat.app, stat.uri " +
            "order by count(stat.ip) desc")
    List<RequestStat> getAllStatistic(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.explore.stat.module.RequestStat(stat.app, stat.uri, count(stat.ip)) " +
            "from Stat stat " +
            "where stat.dateTimeIncome between :start and :end " +
            "and (stat.uri in :uris OR :uris = null) " +
            "group by stat.app, stat.uri " +
            "order by count(stat.ip) desc")
    List<RequestStat> getAllStatisticNonUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
