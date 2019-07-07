package com.arrow.pegasus.repo;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.data.event.Event;

public interface EventRepositoryExtension extends RepositoryExtension<Event> {
	long deleteBefore(Instant time);

	Page<Event> findEvents(Pageable pageable, EventSearchParams params);
}
