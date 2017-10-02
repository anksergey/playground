package com.vk.dal.domain;

import org.hibernate.annotations.AttributeAccessor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AttributeAccessor("field")
@Table(name = "customer")
public class Customer implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false, insertable = true, updatable = true)
  @Type(type = "long")
  private Long id;

  @Column(name = "name")
  private String name;

  public Customer() {
  }

  public Customer(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  //  @Override
  public Long getId() {
    return id;
  }

  //  @Override
  public void setId(Long id) {
    this.id = id;
  }
}
