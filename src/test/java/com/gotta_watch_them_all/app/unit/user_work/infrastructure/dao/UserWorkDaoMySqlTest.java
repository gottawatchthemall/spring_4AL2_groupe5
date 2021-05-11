package com.gotta_watch_them_all.app.unit.user_work.infrastructure.dao;

import com.gotta_watch_them_all.app.core.entity.Media;
import com.gotta_watch_them_all.app.core.entity.Role;
import com.gotta_watch_them_all.app.core.entity.RoleName;
import com.gotta_watch_them_all.app.core.entity.User;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.infrastructure.dao.UserDaoImpl;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.user_work.infrastructure.dao.UserWorkDaoMySql;
import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.entity.UserWorkEntity;
import com.gotta_watch_them_all.app.user_work.infrastructure.dataprovider.repository.UserWorkRepository;
import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.infrastructure.dao.WorkDaoMySql;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserWorkDaoMySqlTest {

    private UserWorkDaoMySql sut;

    private UserWorkRepository mockUserWorkRepository;
    private WorkDaoMySql mockWorkDaoMySql;
    private UserDaoImpl mockUserDao;

    @BeforeEach
    public void setup() {
        mockUserWorkRepository = Mockito.mock(UserWorkRepository.class);
        mockWorkDaoMySql = Mockito.mock(WorkDaoMySql.class);
        mockUserDao = Mockito.mock(UserDaoImpl.class);
        sut = new UserWorkDaoMySql(mockUserWorkRepository, mockUserDao, mockWorkDaoMySql);
    }

    @Test
    public void findById_should_call_user_work_repository_once() throws NotFoundException {
        Mockito.when(mockUserWorkRepository.findByUserIdAndWorkId(1L, 2L)).thenReturn(new UserWorkEntity());
        sut.findById(1L, 2L);
        Mockito.verify(mockUserWorkRepository, Mockito.times(1)).findByUserIdAndWorkId(1L, 2L);
    }

    @Test
    public void findById_should_throw_not_found_excpetion_if_not_found() {
        Mockito.when(mockUserWorkRepository.findByUserIdAndWorkId(1L, 2L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> sut.findById(1L, 2L));
    }

    @Test
    public void findById_should_call_user_dao_once() throws NotFoundException {
        Mockito.when(mockUserWorkRepository.findByUserIdAndWorkId(1L, 2L)).thenReturn(new UserWorkEntity());
        sut.findById(1L, 2L);
        Mockito.verify(mockUserDao, Mockito.times(1)).findById(1L);
    }

    @Test
    public void findById_should_call_work_sql_dao_once() throws NotFoundException {
        Mockito.when(mockUserWorkRepository.findByUserIdAndWorkId(1L, 2L)).thenReturn(new UserWorkEntity());
        sut.findById(1L, 2L);
        Mockito.verify(mockWorkDaoMySql, Mockito.times(1)).findById(2L);
    }

    @Test
    public void findById_should_return_user_work_if_exists() throws NotFoundException {
        Work work = new Work()
                .setId(2L)
                .setImdbId("bjr")
                .setTitle("bjr")
                .setYear("2222")
                .setReleasedDate("demain")
                .setDuration("150min")
                .setGenres("action")
                .setDirectors("action")
                .setWriters("action")
                .setActors("action")
                .setPlot("action")
                .setCountry("action")
                .setAwards("action")
                .setPoster("action")
                .setMedia(new Media().setId(1L))
                .setScore(50);

        User user = new User()
                .setId(1L)
                .setName("robert")
                .setEmail("njzakjhnba@gmail.com")
                .setPassword("njkdcsl")
                .setVulgar(false)
                .setRoles(Set.of(new Role().setName(RoleName.ROLE_USER)));


        UserWorkEntity entity = new UserWorkEntity().setUserId(1L).setWorkId(2L);
        Mockito.when(mockUserWorkRepository.findByUserIdAndWorkId(1L, 2L))
                .thenReturn(entity);
        Mockito.when(mockUserDao.findById(1L)).thenReturn(user);
        Mockito.when(mockWorkDaoMySql.findById(2L)).thenReturn(work);

        UserWork expectedUserWork = new UserWork()
                .setWork(work)
                .setUser(user);

        assertEquals(expectedUserWork, sut.findById(1L, 2L));
    }

}