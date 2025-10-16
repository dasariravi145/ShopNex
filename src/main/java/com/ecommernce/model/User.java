package com.ecommernce.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@Table(name="users",uniqueConstraints= {
		   @UniqueConstraint(columnNames = "username"),
		   @UniqueConstraint(columnNames = "email")
})
public class User {
       @Id
       @GeneratedValue(strategy=GenerationType.IDENTITY)
	   private Long userId;
       
       @NotBlank
       @Size(max=20)
       @Column(name="username")
	   private String userName;
       
       @NotBlank
       @Size(max=30)
	   private String email;
       @NotBlank
       @Size(max=120)
	   private String password;
	   
	   public User(String userName, String email, String password) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
	   }
	   @ManyToMany(cascade= {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
	   @JoinTable(
			     name="USER_ROLE",
			     joinColumns = @JoinColumn(name="USER_ID"),
			     inverseJoinColumns =@JoinColumn(name="ROLE_ID")
			   )
       private Set<Roles> roles=new HashSet<>();
       
	   @OneToMany(mappedBy="user",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
       private List<Address> address=new ArrayList<>();
	   
	   @OneToMany(mappedBy="user",cascade= {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
	   private Set<Product> products;
	   
       @ToString.Exclude
	   @OneToOne(mappedBy="user",cascade= {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
       private Cart cart;
       
       
}
