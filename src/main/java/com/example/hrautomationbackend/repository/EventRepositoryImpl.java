package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.model.EventFormat;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
class EventRepositoryImpl implements EventRepositoryCustom {

    EntityManager em;

    public EventRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public PagedListHolder findEventsByFilter(String name, EventFormat format, CityEntity city, Date fromDate, Date toDate, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);

        Root<EventEntity> event = cq.from(EventEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (city != null) {
            predicates.add(cb.equal(event.get("city"), city));
        }

        if (format != null) {
            predicates.add(cb.equal(event.get("format"), format));
        }
        if (name != null) {
            predicates.add(cb.like(event.get("name"), "%" + name + "%"));
        }
        if (fromDate == null) {
            Calendar calendar = new GregorianCalendar(1000, Calendar.JANUARY, 1);
            fromDate = calendar.getTime();
        }
        if (toDate == null) {
            Calendar calendar = new GregorianCalendar(3000, Calendar.JANUARY, 1);
            toDate = calendar.getTime();
        }
        predicates.add(cb.between(event.get("date"), fromDate, toDate));

        cq.where(predicates.toArray(new Predicate[0]));

        PagedListHolder page = new PagedListHolder(em.createQuery(cq).getResultList());
        page.setPageSize(pageable.getPageSize()); // number of items per page
        page.setPage(pageable.getPageNumber());      // set to first page

        return page;
    }

}
