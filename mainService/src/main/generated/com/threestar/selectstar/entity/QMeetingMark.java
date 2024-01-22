package com.threestar.selectstar.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetingMark is a Querydsl query type for MeetingMark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetingMark extends EntityPathBase<MeetingMark> {

    private static final long serialVersionUID = -1746874702L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetingMark meetingMark = new QMeetingMark("meetingMark");

    public final QMeeting meeting;

    public final NumberPath<Long> meeting_mark_id = createNumber("meeting_mark_id", Long.class);

    public final QUser users;

    public QMeetingMark(String variable) {
        this(MeetingMark.class, forVariable(variable), INITS);
    }

    public QMeetingMark(Path<? extends MeetingMark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetingMark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetingMark(PathMetadata metadata, PathInits inits) {
        this(MeetingMark.class, metadata, inits);
    }

    public QMeetingMark(Class<? extends MeetingMark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meeting = inits.isInitialized("meeting") ? new QMeeting(forProperty("meeting"), inits.get("meeting")) : null;
        this.users = inits.isInitialized("users") ? new QUser(forProperty("users")) : null;
    }

}

