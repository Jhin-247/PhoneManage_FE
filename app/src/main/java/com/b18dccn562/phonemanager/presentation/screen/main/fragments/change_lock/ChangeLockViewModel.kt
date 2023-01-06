package com.b18dccn562.phonemanager.presentation.screen.main.fragments.change_lock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.utils.PatternUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeLockViewModel @Inject constructor(
    private val patternUtil: PatternUtil
) : ViewModel() {
    private val _lockStatus = MutableLiveData<LockPatternStatus>()
    val lockStatus: LiveData<LockPatternStatus> = _lockStatus

    //    private var isInitForUnlockApp = MutableLiveData<Boolean>()
    private var _changed = MutableLiveData(false)
    val changed: LiveData<Boolean> = this._changed

    //    private var lockStatus = MutableLiveData<LockPatternStatus>()
    private var patternToSaved: List<Int> = mutableListOf()

    fun checkPattern(pattern: List<Int>) {
        if (patternToSaved.isEmpty()) {
            if (pattern.size >= 3) {
                patternToSaved = patternToSaved.plus(pattern)
                setLockStatus(LockPatternStatus.REDRAW_PATTERN)
            } else {
                setLockStatus(LockPatternStatus.TOO_SHORT)
            }
        } else {
            if (patternToSaved == pattern) {
                patternUtil.savePattern(pattern)
                this._changed.value = true
            } else {
                setLockStatus(LockPatternStatus.WRONG_PATTERN)
            }
        }
    }

    fun setLockStatus(status: LockPatternStatus) {
        _lockStatus.value = status
    }

}