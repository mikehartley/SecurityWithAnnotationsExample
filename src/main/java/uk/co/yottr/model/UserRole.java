package uk.co.yottr.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

//TODO http://www.mkyong.com/spring-security/spring-security-hibernate-annotation-example/

/*
 * Copyright (c) 2014. Mike Hartley Solutions Ltd
 * All rights reserved.
 */

@Entity
@Table(name = "user_roles",
       uniqueConstraints = @UniqueConstraint(columnNames = {"role", "username"}))
public class UserRole {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Column(name = "role", nullable = false, length = 45)
    private String role;

    public UserRole() {
        // required by hibernate
    }

    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public String getRole() {
        return this.role;
    }
}

