package com.threestar.selectstar.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApply is a Querydsl query type for Apply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApply extends EntityPathBase<Apply> {

    private static final long serialVersionUID = -911155112L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApply apply = new QApply("apply");

    public final DatePath<java.sql.Date> applicationDate = createDate("applicationDate", java.sql.Date.class);

    public final QApplyID applyID;

    public final StringPath emailAddress = createString("emailAddress");

    public final StringPath reason = createString("reason");

    public final NumberPath<Integer> reject = createNumber("reject", Integer.class);

    public final StringPath rejectReason = createString("rejectReason");

    public final StringPath snsAddress = createString("snsAddress");

    public QApply(String variable) {
        this(Apply.class, forVariable(variable), INITS);
    }

    public QApply(Path<? extends Apply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApply(PathMetadata metadata, PathInits inits) {
        this(Apply.class, metadata, inits);
    }

    public QApply(Class<? extends Apply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.applyID = inits.isInitialized("applyID") ? new QApplyID(forProperty("applyID"), inits.get("applyID")) : null;
    }

}

