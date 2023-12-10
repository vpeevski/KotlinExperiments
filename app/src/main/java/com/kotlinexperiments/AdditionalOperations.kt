//package com.kotlinexperiments
//
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//
//// TODO Make it work
//// Starting operations on an injected scope object is common. Passing
//// a scope clearly signals that such a class can start independent calls.
//// This means suspending functions might not wait for all the operations
//// they start. If no scope is passed, we can expect that suspending
//// functions will not finish until all their operations are done.
//// class ShowUserDataUseCase(
//    private val repo: UserDataRepository,
//    private val view: UserDataView,
//    private val analyticsScope: CoroutineScope,
//) {
//    suspend fun showUserData() = coroutineScope {
//        val name = async { repo.getName() }
//        val friends = async { repo.getFriends() }
//        val profile = async { repo.getProfile() }
//        val user = User(
//            name = name.await(),
//            friends = friends.await(),
//            profile = profile.await()
//        )
//        view.show(user)
//        analyticsScope.launch { repo.notifyProfileShown() } // Not waited to finish. If throw does not affect essential operations.
//    }
//}