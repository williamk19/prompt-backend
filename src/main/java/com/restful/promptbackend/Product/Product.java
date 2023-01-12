package com.restful.promptbackend.Product;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 255)
  @Length(max = 255)
  private String productName;

  private float price;

  public Product() {
  }

  public Product(String productName, float price) {
    this.productName = productName;
    this.price = price;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }
}
