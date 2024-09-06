package org.zerocool.postservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerocool.postservice.adapter.entity.CounterPost;
import org.zerocool.postservice.adapter.repository.CounterPostRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CounterPostService {
    private final CounterPostRepository counterPostRepository;

    public Mono<Long> getNextSequenceValue(String sequenceName) {
        return counterPostRepository.findById(sequenceName)
                .flatMap(counter -> {
                    counter.setSeq(counter.getSeq() + 1);
                    return counterPostRepository.save(counter)
                            .thenReturn(counter.getSeq());
                })
                .switchIfEmpty(
                        counterPostRepository.save(new CounterPost(sequenceName, 1))
                                .thenReturn(1L)
                );
    }
}
