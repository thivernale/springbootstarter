package org.thivernale.springbootstarter.security.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
    @NamedQuery(name = "User.byId", query = "from User where id = ?0")
})
@NamedNativeQueries({
    @NamedNativeQuery(name= "User.byName", query = "SELECT * FROM users WHERE username = ?", resultClass = User.class)
})
@Table(name = "users")
@SecondaryTable(name = "authorities", pkJoinColumns = @PrimaryKeyJoinColumn(name="username"))
@Transactional()
@SelectBeforeUpdate()
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;
    /**
     * natural key - has business logic (i.e. username, email)
     * surrogate key - added just to serve as key
     */
    @Id
    @PrimaryKeyJoinColumn()
    @Column(name = "username")
    // without additional properties it is equal to not specifying a mapping annotation
    @Basic()
    private String userName;
    private String password;
    @Column(name = "enabled")
    private boolean active;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date added;

    // field is not persistent (it will be ignored by Hibernate)
    @Transient
    private String displayName;

    @Column(table = "authorities", name = "authority")
    @Lob
    @NotFound(action = NotFoundAction.IGNORE)
    private String roles;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "username")
        }
        )
    /**
     * Not part of JPA standard (javax.persistence package), but annotations
     * specific to Hibernate
     */
    /**
     * Provided by Hibernate for autogeneration of identifiers
     */
    @GenericGenerator(
        name = "hilo_gen",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator"
        )
    /**
     * Define an identifier for the collection
     */
    @CollectionId(
        columns = {
            @Column(name = "role_id")
        },
        generator = "hilo_gen",
        type = @Type(type = "long")
        )
    /**
     * In order to have an index field in the table, use a collection which
     * supports indexes for the member variable of the Entity class
     */
    private Collection<UserRole> userRoles = new ArrayList<UserRole>();

    /**
     * We can have embedded value types, which are not entities
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "home_street")),
        @AttributeOverride(name = "city", column = @Column(name = "home_city")),
        @AttributeOverride(name = "state", column = @Column(name = "home_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip_code")),
    })
    Address address;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "office_street")),
        @AttributeOverride(name = "city", column = @Column(name = "office_city")),
        @AttributeOverride(name = "state", column = @Column(name = "office_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "office_zip_code"))
    })
    Address officeAddress;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", insertable = true, updatable = true)
    private Employee employee;

    /**
     * Declaration has to be an interface, not an implementation
     */
    @ElementCollection
    @JoinTable(
        name = "user_address",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "username")
        }/*,
        inverseJoinColumns = {
            @JoinColumn(name = "address_id")
        }*/
        )
    @AttributeOverrides({
        @AttributeOverride(name = "user", column = @Column(name = "user_id"))
    })
    private Set<Address> listOfAddresses = new HashSet<Address>();

    public User() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Date getAdded() {
        return added;
    }
    public void setAdded(Date added) {
        if (added == null) {
            added = new Date();
        }
        this.added = added;
    }
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
    public Collection<UserRole> getUserRoles() {
        return userRoles;
    }
    public void setUserRoles(Collection<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    public String getDisplayName() {
        return displayName != null ? displayName : userName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public Address getOfficeAddress() {
        return officeAddress;
    }
    public void setOfficeAddress(Address officeAddress) {
        this.officeAddress = officeAddress;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Set<Address> getListOfAddresses() {
        return listOfAddresses;
    }
    public void setListOfAddresses(Set<Address> listOfAddresses) {
        this.listOfAddresses = listOfAddresses;
    }
}
