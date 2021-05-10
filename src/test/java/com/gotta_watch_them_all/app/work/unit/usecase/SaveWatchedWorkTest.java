package com.gotta_watch_them_all.app.work.unit.usecase;

import com.gotta_watch_them_all.app.work.usecase.SaveWatchedWork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaveWatchedWorkTest {

    private SaveWatchedWork sut;

    @BeforeEach
    public void setup() {
        sut = new SaveWatchedWork();
    }

    @Test
    public void execute_should_throw_not_found_exception_if_id_incorrect() {

    }

    @Test
    public void execute_should_throw_already_exists_exception_if_already_saved() {

    }

    @Test
    public void execute_should_call_work_dao_find_from_api_with_details_once() {

    }

    @Test
    public void execute_should_return_all_work_details() {

    }
}
