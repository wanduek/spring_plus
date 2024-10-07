package org.example.expert.domain.common.service;

import org.example.expert.domain.common.entity.Log;
import org.example.expert.domain.common.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    // 독립적인 트랜잭션으로 로그를 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(Long managerId, String manager, String action, String message) {
        Log log = new Log(managerId, manager, action, message);
        logRepository.save(log);
    }
}
