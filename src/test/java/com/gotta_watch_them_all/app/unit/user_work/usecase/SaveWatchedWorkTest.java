package com.gotta_watch_them_all.app.unit.user_work.usecase;

import com.gotta_watch_them_all.app.core.entity.User;
import com.gotta_watch_them_all.app.core.exception.NotFoundException;
import com.gotta_watch_them_all.app.infrastructure.dao.UserDaoImpl;
import com.gotta_watch_them_all.app.user_work.usecase.SaveWatchedWork;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gotta_watch_them_all.app.work.infrastructure.dao.WorkDaoMySql;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SaveWatchedWorkTest {

    private SaveWatchedWork sut;

    private UserDaoImpl mockUserDao;
    private WorkDaoMySql mockWorkDaoMySql;

    @BeforeEach
    public void setup() {
        mockUserDao = Mockito.mock(UserDaoImpl.class);
        mockWorkDaoMySql = Mockito.mock(WorkDaoMySql.class);
        sut = new SaveWatchedWork(mockUserDao, mockWorkDaoMySql);
    }

    @Test
    public void execute_should_call_user_dao_find_by_id_once() throws NotFoundException {
        Mockito.when(mockUserDao.findById(1L)).thenReturn(new User());
        sut.execute(1L, "yo");
        Mockito.verify(mockUserDao, Mockito.times(1)).findById(1L);
    }
//
//    @Test
//    public void execute_should_call_mysql_work_dao_find_by_imdbid_once() throws NotFoundException {
//        Mockito.when(mockUserDao.findById(1L)).thenReturn(new User());
//        sut.execute(1L, "yo");
//        Mockito.verify(mockWorkDaoMySql, Mockito.times(1)).findByImdbId(Mockito.anyString());
//    }

    //    @Test
//    public void execute_should_call_work_dao_find_from_api_with_details_once() {
////        sut.execute(1L, "yo");
////        Mockito.verify(userDao.)
//    }
//
//
//
//    @Test
//    public void execute_should_call_work_dao_mysql_find_by_id_once() {
//
//    }
//
//    @Test
//    public void execute_should_call_work_dao_mysql_save_once_if_not_already_save() {
//
//    }
//
    @Test
    public void execute_should_throw_not_found_exception_if_id_incorrect() throws NotFoundException {
        Mockito.when(mockUserDao.findById(2L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> sut.execute(2L, "yo"));
    }
//
//    @Test
//    public void execute_should_throw_already_exists_exception_if_already_saved() {
//
//    }
//
//
//    @Test
//    public void execute_should_return_all_work_details() {
//
//    }
}
