package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.model.EventFormat;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
class EventRepositoryImpl implements EventRepositoryCustom {

    EntityManager em;

    public EventRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<EventEntity> findEventsByNameAndFormat(String name, EventFormat format) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);

        Root<EventEntity> event = cq.from(EventEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (format != null) {
            predicates.add(cb.equal(event.get("format"), format));
        }
        if (name != null) {
            predicates.add(cb.like(event.get("name"), "%" + name + "%"));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

}
