/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilAgenda;
import org.egov.council.repository.CouncilAgendaRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilAgendaService {

    private final CouncilAgendaRepository councilAgendaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public CouncilAgendaService(final CouncilAgendaRepository councilAgendaRepository) {
        this.councilAgendaRepository = councilAgendaRepository;
    }

    @Transactional
    public CouncilAgenda create(final CouncilAgenda councilAgenda) {
        return councilAgendaRepository.save(councilAgenda);
    }

    @Transactional
    public CouncilAgenda update(final CouncilAgenda councilAgenda) {
        return councilAgendaRepository.save(councilAgenda);
    }

    public List<CouncilAgenda> findAll() {
        return councilAgendaRepository.findAll(new Sort(Sort.Direction.DESC, "agendaNumber"));
    }

    public CouncilAgenda findOne(Long id) {
        return councilAgendaRepository.findById(id);
    }

    public List<CouncilAgenda> search(CouncilAgenda councilAgenda) {
        final Criteria criteria = getCurrentSession().createCriteria(CouncilAgenda.class);
        if (null != councilAgenda.getAgendaNumber())
            criteria.add(Restrictions.ilike("agendaNumber", councilAgenda.getAgendaNumber(), MatchMode.ANYWHERE));
        return criteria.list();
    }

}