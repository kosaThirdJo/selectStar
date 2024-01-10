package com.threestar.selectstar.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1356679585L;

    public static final QUser user = new QUser("user");

    public final StringPath aboutMe = createString("aboutMe");

    public final NumberPath<Integer> deleted = createNumber("deleted", Integer.class);

    public final StringPath email = createString("email");

    public final StringPath interestFramework = createString("interestFramework");

    public final StringPath interestJob = createString("interestJob");

    public final StringPath interestLanguage = createString("interestLanguage");

    public final DatePath<java.sql.Date> joinDate = createDate("joinDate", java.sql.Date.class);

    public final StringPath location1 = createString("location1");

    public final StringPath location2 = createString("location2");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileContent = createString("profileContent");

    public final ArrayPath<byte[], Byte> profilePhoto = createArray("profilePhoto", byte[].class);

    public final StringPath role = createString("role");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

