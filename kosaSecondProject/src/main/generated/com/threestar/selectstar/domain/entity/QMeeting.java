package com.threestar.selectstar.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeeting is a Querydsl query type for Meeting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeeting extends EntityPathBase<Meeting> {

    private static final long serialVersionUID = -2006444891L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeeting meeting = new QMeeting("meeting");

    public final NumberPath<Integer> applicationCount = createNumber("applicationCount", Integer.class);

    public final DatePath<java.sql.Date> applicationDeadline = createDate("applicationDeadline", java.sql.Date.class);

    public final NumberPath<Integer> category = createNumber("category", Integer.class);

    public final DatePath<java.sql.Date> creationDate = createDate("creationDate", java.sql.Date.class);

    public final NumberPath<Integer> deleted = createNumber("deleted", Integer.class);

    public final StringPath description = createString("description");

    public final StringPath interestFramework = createString("interestFramework");

    public final StringPath interestJob = createString("interestJob");

    public final StringPath interestLanguage = createString("interestLanguage");

    public final StringPath location = createString("location");

    public final NumberPath<Integer> meetingId = createNumber("meetingId", Integer.class);

    public final NumberPath<Integer> recruitmentCount = createNumber("recruitmentCount", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath title = createString("title");

    public final QUser user;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QMeeting(String variable) {
        this(Meeting.class, forVariable(variable), INITS);
    }

    public QMeeting(Path<? extends Meeting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeeting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeeting(PathMetadata metadata, PathInits inits) {
        this(Meeting.class, metadata, inits);
    }

    public QMeeting(Class<? extends Meeting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

