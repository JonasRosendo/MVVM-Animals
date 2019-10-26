package com.jonasrosendo.mvvmanimals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jonasrosendo.mvvmanimals.model.api.AnimalApiService
import com.jonasrosendo.mvvmanimals.model.api.ApiKey
import com.jonasrosendo.mvvmanimals.model.model.Animal
import com.jonasrosendo.mvvmanimals.util.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AnimalListViewModel(application: Application) : AndroidViewModel(application) {

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val hasLoadError by lazy { MutableLiveData<Boolean>() }
    val isLoading by lazy { MutableLiveData<Boolean>() }
    var invalidApiKey = false

    private val disposable = CompositeDisposable()

    private val apiService = AnimalApiService()

    private val prefs = SharedPreferenceHelper(getApplication())

    fun refresh(){
        isLoading.value = true
        invalidApiKey = false
        val key = prefs.getApiKey()
        if(key.isNullOrEmpty()){
            getKey()
        }else{
            getAnimals(key)
        }
    }

    fun hardRefresh(){
        isLoading.value = true
        getKey()
    }

    private fun getKey(){
        disposable.add(
            apiService.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(apiKey: ApiKey) {
                        if(apiKey.key.isNullOrEmpty()){
                            hasLoadError.value = true
                            isLoading.value = false
                        }else{
                            prefs.saveApiKey(apiKey.key)
                            getAnimals(apiKey.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        isLoading.value = false
                        hasLoadError.value = true
                    }
                })
        )
    }

    private fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>(){
                    override fun onSuccess(list: List<Animal>) {
                        if(list.isEmpty()){
                            isLoading.value = false
                            hasLoadError.value = true
                            animals.value = null
                        }else{
                            isLoading.value = false
                            hasLoadError.value = false
                            animals.value = list
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if(!invalidApiKey){
                            invalidApiKey = true
                            getKey()
                        }else{
                            isLoading.value = false
                            hasLoadError.value = true
                        }
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}