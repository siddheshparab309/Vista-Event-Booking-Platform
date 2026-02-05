package com.events.superapp.common.repository;

import com.events.superapp.common.entity.BaseEvent;
import com.events.superapp.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseEventRepository extends JpaRepository<BaseEvent, Long> {
}
