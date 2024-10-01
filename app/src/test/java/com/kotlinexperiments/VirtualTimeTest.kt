//package com.kotlinexperiments
//
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.test.advanceTimeBy
//import kotlinx.coroutines.test.currentTime
//import kotlinx.coroutines.test.runTest
//import org.junit.Test
//
//class FetchUserDataTest {
//    @Test
//    fun `should load data concurrently`() = runTest {
//// given
//        val userRepo = FakeUserDataRepository()
//        val useCase = FetchUserUseCase(userRepo)
//// when
//        useCase.fetchUserData()
//// then
//        assertEquals(5000, currentTime)
//    }
//
//    @Test
//    fun `should construct user`() = runTest {
//// given
//        val userRepo = FakeUserDataRepository()
//        val useCase = FetchUserUseCase(userRepo)
//        advanceTimeBy(5000)
//// when
//        val result = useCase.fetchUserData()
//// then
//        val expectedUser = User(
//            name = "Ben",
//            friends = listOf(Friend("some-friend-id-1")),
//            profile = Profile("Example description")
//        )
//        assertEquals(expectedUser, result)
//    }
//}
//
//class FakeUserDataRepository : UserDataRepository {
//    override suspend fun getName(): String {
//        kotlinx.coroutines.delay(5000)
//        return "Ben"
//    }
//
//    override suspend fun getFriends(): List<Friend> {
//        kotlinx.coroutines.delay(5000)
//        return listOf(Friend("some-friend-id-1"))
//    }
//
//    override suspend fun getProfile(): Profile {
//        kotlinx.coroutines.delay(5000)
//        return Profile("Example description")
//    }
//}