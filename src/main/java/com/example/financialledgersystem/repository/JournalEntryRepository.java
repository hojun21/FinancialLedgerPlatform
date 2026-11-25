package com.example.financialledgersystem.repository;

import com.example.financialledgersystem.domain.Direction;
import com.example.financialledgersystem.domain.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    /* JPQL query */
    @Query("select coalesce(sum(l.amount), 0L) from LedgerLine l where l.direction = :direction")
    long sumByDirection(@Param("direction") Direction direction);

}
