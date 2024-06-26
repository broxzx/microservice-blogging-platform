package com.example.blogservice.service;

import com.example.blogservice.entity.DatabaseSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * The SequenceGeneratorService class is responsible for generating unique sequences for a given sequence name.
 */
@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    /**
     * Generates a unique sequence for a given sequence name.
     *
     * @param sequenceName A string representing the name of the sequence.
     * @return A long value representing the generated sequence.
     */
    public long generateSequence(String sequenceName) {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(sequenceName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
