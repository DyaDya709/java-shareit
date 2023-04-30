package ru.practicum.shareit.request.response.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Profile("test")
public class ItemRequestServiceImplTest {

}
