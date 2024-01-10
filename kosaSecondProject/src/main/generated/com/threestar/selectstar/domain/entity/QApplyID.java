package com.threestar.selectstar.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApplyID is a Querydsl query type for ApplyID
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QApplyID extends BeanPath<ApplyID> {

    private static final long serialVersionUID = 553268083L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApplyID applyID = new QApplyID("applyID");

    public final QMeeting meeting;

    public final QUser user;

    public QApplyID(String variable) {
        this(ApplyID.class, forVariable(variable), INITS);
    }

    public QApplyID(Path<? extends ApplyID> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApplyID(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApplyID(PathMetadata metadata, PathInits inits) {
        this(ApplyID.class, metadata, inits);
    }

    public QApplyID(Class<? extends ApplyID> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meeting = inits.isInitialized("meeting") ? new QMeeting(forProperty("meeting"), inits.get("meeting")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

