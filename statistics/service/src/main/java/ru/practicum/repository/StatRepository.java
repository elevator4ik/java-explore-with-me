package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.RequestStat;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Integer>, JpaSpecificationExecutor<Stat> {

    @Query("select app as app, uri As uri, COUNT(distinct ip) AS hits " +
            "from Stat " +
            "where (dateTimeIncome BETWEEN :start AND :end) AND (uri IN :uris OR :uris = NULL) " +
            "GROUP BY app, uri " +
            "ORDER BY COUNT(DISTINCT ip) DESC")
    List<RequestStat> getAllStatistic(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select ST.app AS app, ST.uri AS uri, COUNT(ST.id) AS hits " +
            "from Stat AS ST " +
            "where (ST.dateTimeIncome BETWEEN :start AND :end) AND (ST.uri IN :uris OR :uris = NULL) " +
            "GROUP BY app, uri " +
            "ORDER BY COUNT(ST.id) DESC")
    List<RequestStat> getAllStatisticNonUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
