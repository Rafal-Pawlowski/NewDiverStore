package com.ralf.NewDiverStore.customer.service;

import com.ralf.NewDiverStore.customer.domain.model.BillingAddress;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.model.CustomerBuilder;
import com.ralf.NewDiverStore.customer.domain.model.ShippingAddress;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateCustomer() {
        //given
        Customer customerRequest = new CustomerBuilder()
                .withFirstName("Customer")
                .withLastName("LastName")
                .withEmail("email")
                .build();
        //when
        Customer result = customerService.createCustomer(customerRequest);

        //then
        assertThat(result).isNotNull()
                .extracting(Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                .containsExactly(customerRequest.getFirstName(), customerRequest.getLastName(), customerRequest.getEmail());
    }

    @Test
    void shouldCreateNewCustomerWithNamesAndEmailWhenNotExistOne() {
        //given
        Customer customerRequest = new CustomerBuilder()
                .withFirstName("Customer")
                .withLastName("LastName")
                .withEmail("email")
                .build();

        //when
        Customer result = customerService.createOrUpdateCustomerWithNamesAndEmail(customerRequest);

        //then
        assertThat(result).isNotNull()
                .extracting(Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                .containsExactly(customerRequest.getFirstName(), customerRequest.getLastName(), customerRequest.getEmail());
        assertThat(customerRepository.findById(result.getId())).isPresent();
    }

    @Test
    void shouldUpdateExistingCustomerWithNamesAndEmail() {
        //given
        Customer customerRequest = new CustomerBuilder()
                .withFirstName("Customer")
                .withLastName("LastName")
                .withEmail("email")
                .build();
        Customer customer = customerRepository.save(
                new Customer("NameBeforeUpdate", "LastNameBeforeUpdate", "EmailBeforeUpdate"));
        customerRequest.setId(customer.getId());

        //when
        Customer result = customerService.createOrUpdateCustomerWithNamesAndEmail(customerRequest);

        //then
        assertThat(result).isNotNull()
                .extracting(Customer::getId, Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                .containsExactly(customer.getId(), customerRequest.getFirstName(), customerRequest.getLastName(), customerRequest.getEmail());

    }

    @Test
    void shouldGetCustomers() {
        //given
        Customer customer1 = new Customer("Customer1", "LastName1", "email1");
        Customer customer2 = new Customer("Customer2", "LastName2", "email2");
        Customer customer3 = new Customer("Customer3", "LastName3", "email3");
        customerRepository.saveAll(List.of(customer1, customer2, customer3));

        //when
        Page<Customer> result = customerService.getCustomers(Pageable.unpaged());

        //then

        assertThat(result).hasSize(3)
                .extracting(Customer::getFirstName)
                .containsExactly(customer1.getFirstName(), customer2.getFirstName(), customer3.getFirstName());

    }

    @Test
    void shouldGetExistingCustomer() {
        //given
        Customer customer1 = customerRepository.save(new Customer("Customer1", "LastName1", "email1"));

        //when
        Customer result = customerService.getCustomer(customer1.getId());

        //then
        assertThat(result).isNotNull()
                .extracting(Customer::getId, Customer::getFirstName, Customer::getLastName, Customer::getEmail)
                .containsExactly(customer1.getId(), customer1.getFirstName(), customer1.getLastName(), customer1.getEmail());

    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhileGettingNonExistingCustomer() {
        //given
        UUID id = UUID.randomUUID();

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> customerService.getCustomer(id));

        assertThat(exception.getMessage()).isEqualTo("Customer with id: " + id + " not found");

    }

    @Test
    void shouldUpdateCustomer() {
        //given
        Customer customerRequest = new CustomerBuilder()
                .withFirstName("Customer")
                .withLastName("LastName")
                .withEmail("email")
                .withShippingAddress(new ShippingAddress("Mickiewicza", "Poland", "Warsaw", "04-001"))
                .withBillingAddress(new BillingAddress("Walaszka", "Poland", "Warsaw", "02-001"))
                .build();
        Customer customer = customerRepository.save(new Customer());

        //when
        Customer result = customerService.updateCustomer(customer.getId(), customerRequest);

        //then
        assertThat(result).isNotNull()
                .extracting(Customer::getId, Customer::getFirstName, Customer::getLastName,
                        Customer::getEmail)
                .containsExactly(customer.getId(), customerRequest.getFirstName(), customerRequest.getLastName(),
                        customerRequest.getEmail());
        assertThat(result)
                .extracting(Customer::getShippingAddress)
                .extracting(ShippingAddress::getStreet, ShippingAddress::getCity, ShippingAddress::getCountry, ShippingAddress::getZip)
                .containsExactly(customerRequest.getShippingAddress().getStreet(),
                        customerRequest.getShippingAddress().getCity(),
                        customerRequest.getShippingAddress().getCountry(),
                        customerRequest.getShippingAddress().getZip());
        assertThat(result)
                .extracting(Customer::getBillingAddress)
                .extracting(BillingAddress::getStreet, BillingAddress::getCity, BillingAddress::getCountry, BillingAddress::getZip)
                .containsExactly(customerRequest.getBillingAddress().getStreet(),
                        customerRequest.getBillingAddress().getCity(),
                        customerRequest.getBillingAddress().getCountry(),
                        customerRequest.getBillingAddress().getZip());
        assertThat(result.getShippingAddress()).isEqualTo(customer.getShippingAddress());

    }

    @Test
    void shouldDeleteExistingCustomer() {
        //given
        Customer customer = customerRepository.save(new Customer("Customer", "LastName", "email@email"));
        UUID id = customer.getId();

        //when
        Throwable throwable = catchThrowable(()-> customerService.deleteCustomer(id));

        //then
        assertThat(throwable).isNull();
        assertThat(customerRepository.findById(id)).isEmpty();

    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenRemovingNonExistingCustomer() {
        //given
        UUID id = UUID.randomUUID();

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerService.deleteCustomer(id);
        });
        assertThat(exception.getMessage()).isEqualTo("Customer with id: " + id + " not exist");

    }


}