package com.gotta_watch_them_all.app.unit.user_work.usecase;

import com.gotta_watch_them_all.app.core.entity.User;
import com.gotta_watch_them_all.app.core.exception.AlreadyCreatedException;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.infrastructure.dao.UserDaoImpl;
import com.gotta_watch_them_all.app.user_work.core.entity.UserWork;
import com.gotta_watch_them_all.app.user_work.infrastructure.dao.UserWorkDaoMySql;
import com.gotta_watch_them_all.app.user_work.usecase.SaveWatchedWork;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gotta_watch_them_all.app.work.core.entity.Work;
import com.gotta_watch_them_all.app.work.core.exception.AnySearchValueFoundException;
import com.gotta_watch_them_all.app.work.core.exception.IllegalImdbIdGivenException;
import com.gotta_watch_them_all.app.work.core.exception.TooManySearchArgumentsException;
import com.gotta_watch_them_all.app.work.infrastructure.dao.WorkDaoMovieDbApi;
import com.gotta_watch_them_all.app.work.infrastructure.dao.WorkDaoMySql;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SaveWatchedWorkTest {

    private SaveWatchedWork sut;

    private UserDaoImpl mockUserDao;
    private WorkDaoMySql mockWorkDaoMySql;
    private WorkDaoMovieDbApi mockWorkDaoMovieDbApi;
    private UserWorkDaoMySql mockUserWorkDaoMySql;

    @BeforeEach
    public void setup() {
        mockUserDao = Mockito.mock(UserDaoImpl.class);
        mockWorkDaoMySql = Mockito.mock(WorkDaoMySql.class);
        mockWorkDaoMovieDbApi = Mockito.mock(WorkDaoMovieDbApi.class);
        mockUserWorkDaoMySql = Mockito.mock(UserWorkDaoMySql.class);
        sut = new SaveWatchedWork(mockUserDao, mockWorkDaoMySql, mockWorkDaoMovieDbApi, mockUserWorkDaoMySql);
    }

    @Test
    public void execute_should_call_user_dao_find_by_id_once() throws NotFoundException, IllegalImdbIdGivenException, AlreadyCreatedException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Mockito.when(mockUserDao.findById(1L)).thenReturn(new User());
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(new Work());
        sut.execute(1L, "yo");
        Mockito.verify(mockUserDao, Mockito.times(1)).findById(1L);
    }

    @Test
    public void execute_should_throw_not_found_exception_if_id_incorrect() throws NotFoundException {
        Mockito.when(mockUserDao.findById(2L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> sut.execute(2L, "yo"));
    }


    @Test
    public void execute_should_call_mysql_work_dao_find_by_imdbid_once() throws NotFoundException, IllegalImdbIdGivenException, AlreadyCreatedException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Mockito.when(mockUserDao.findById(1L)).thenReturn(new User());
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(new Work());
        sut.execute(1L, "yo");
        Mockito.verify(mockWorkDaoMySql, Mockito.times(1)).findByImdbId(Mockito.anyString());
    }

    @Test
    public void execute_should_not_call_work_dao_api_if_found_in_db() throws IllegalImdbIdGivenException, NotFoundException, AlreadyCreatedException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Mockito.when(mockUserDao.findById(1L)).thenReturn(new User());
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(new Work());
        sut.execute(1L, "yo");
        Mockito.verify(mockWorkDaoMovieDbApi, Mockito.times(0)).findByImdbId(Mockito.anyString());
    }

    @Test
    public void execute_should_call_work_dao_api_if_not_found_in_db() throws IllegalImdbIdGivenException, NotFoundException, AlreadyCreatedException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Mockito.when(mockUserDao.findById(1L)).thenReturn(new User());
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(null);
        Mockito.when(mockWorkDaoMovieDbApi.findByImdbId("yo")).thenReturn(new Work());
        Mockito.when(mockWorkDaoMySql.save(Mockito.any())).thenReturn(new Work());
        sut.execute(1L, "yo");
        Mockito.verify(mockWorkDaoMovieDbApi, Mockito.times(1)).findByImdbId(Mockito.anyString());
    }

    @Test
    public void execute_should_throw_illegal_imdbid_if_not_found_nowhere() throws IllegalImdbIdGivenException, NotFoundException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Mockito.when(mockUserDao.findById(1L)).thenReturn(new User());
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(null);
        Mockito.when(mockWorkDaoMovieDbApi.findByImdbId("yo")).thenReturn(null);
        assertThrows(IllegalImdbIdGivenException.class, () -> sut.execute(1L, "yo"));
    }

    @Test
    public void execute_should_call_user_work_dao_find_once() throws NotFoundException, IllegalImdbIdGivenException, AlreadyCreatedException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Mockito.when(mockUserDao.findById(1L)).thenReturn(new User().setId(1L));
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(new Work().setId(2L));
        sut.execute(1L, "yo");
        Mockito.verify(mockUserWorkDaoMySql, Mockito.times(1))
                .findById(1L, 2L);
    }

    @Test
    public void execute_should_throw_already_exists_exception_if_already_saved() throws IllegalImdbIdGivenException, NotFoundException {
        Work work = new Work().setId(2L);
        User user = new User().setId(1L);
        Mockito.when(mockUserDao.findById(1L)).thenReturn(user);
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(work);
        Mockito.when(mockUserWorkDaoMySql.findById(1L, 2L)).thenReturn(
                new UserWork().setWork(work).setUser(user)
        );
        assertThrows(AlreadyCreatedException.class, () -> sut.execute(1L, "yo"));
    }

    @Test
    public void execute_should_call_work_dao_mysql_save_once_if_not_already_save() throws NotFoundException, IllegalImdbIdGivenException, AlreadyCreatedException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Work work = new Work().setId(2L).setImdbId("yo");
        User user = new User().setId(1L);
        UserWork userWork = new UserWork().setWork(work).setUser(user);
        Mockito.when(mockUserDao.findById(1L)).thenReturn(user);
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(work);
        Mockito.when(mockUserWorkDaoMySql.findById(1L, 2L))
                .thenReturn(null);
        sut.execute(1L, "yo");
        Mockito.verify(mockUserWorkDaoMySql, Mockito.times(1))
                .save(userWork);
    }

    @Test
    public void execute_should_return_user_work() throws NotFoundException, IllegalImdbIdGivenException, AlreadyCreatedException, AnySearchValueFoundException, TooManySearchArgumentsException {
        Work work = new Work().setId(2L).setImdbId("yo");
        User user = new User().setId(1L);
        UserWork userWork = new UserWork().setWork(work).setUser(user);
        Mockito.when(mockUserDao.findById(1L)).thenReturn(user);
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(work);
        Mockito.when(mockUserWorkDaoMySql.findById(1L, 2L))
                .thenReturn(null);
        Mockito.when(mockUserWorkDaoMySql.save(userWork))
                .thenReturn(userWork);
        assertEquals(userWork, sut.execute(1L, "yo"));
    }

    @Test
    public void execute_should_call_repo_save_work_if_not_exists_in_db() throws NotFoundException, IllegalImdbIdGivenException, AnySearchValueFoundException, TooManySearchArgumentsException, AlreadyCreatedException {
        Work work = new Work().setId(2L).setImdbId("yo");
        User user = new User().setId(1L);
        Mockito.when(mockUserDao.findById(1L)).thenReturn(user);
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(null);
        Mockito.when(mockWorkDaoMovieDbApi.findByImdbId("yo"))
                .thenReturn(work);
        Mockito.when(mockWorkDaoMySql.save(work)).thenReturn(work);
        sut.execute(1L, "yo");
        Mockito.verify(mockWorkDaoMySql, Mockito.times(1))
                .save(work);
    }

    @Test
    public void execute_should_not_call_repo_save_work_if_exists_in_db() throws NotFoundException, IllegalImdbIdGivenException, AnySearchValueFoundException, TooManySearchArgumentsException, AlreadyCreatedException {
        Work work = new Work().setId(2L).setImdbId("yo");
        User user = new User().setId(1L);
        Mockito.when(mockUserDao.findById(1L)).thenReturn(user);
        Mockito.when(mockWorkDaoMySql.findByImdbId("yo")).thenReturn(work);
        Mockito.when(mockWorkDaoMovieDbApi.findByImdbId("yo"))
                .thenReturn(work);
        sut.execute(1L, "yo");
        Mockito.verify(mockWorkDaoMySql, Mockito.times(0))
                .save(work);
    }
}
