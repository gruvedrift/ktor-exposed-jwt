//import com.gruvedrift.domain.dto.request.CreateDroidRequest
//import com.gruvedrift.domain.dto.request.UpdateDroidRequest
//import com.gruvedrift.exception.EntityNotFoundException
//import com.gruvedrift.repository.DroidRepository
//import com.gruvedrift.service.DroidService
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.assertAll
//import org.junit.jupiter.api.assertThrows
//import utils.BaseTestConfiguration
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class DroidServiceTest: BaseTestConfiguration() {
//
//    private lateinit var droidService: DroidService
//
//    @BeforeAll
//    fun setup() { droidService = DroidService(DroidRepository()) }
//
//    @Test
//    fun `getDroidById returns expected droid`() {
//        val droid = droidService.getDroidById(1)
//        assertAll(
//            { assertEquals(1, droid.id) },
//            { assertEquals("Industrial Automaton", droid.manufacturer) },
//            { assertEquals(5000, droid.price) },
//            { assertEquals(1, droid.pitCrewId) }
//        )
//    }
//
//    @Test
//    fun `getAllByPilotId returns correct droids for Sebulba`() {
//        val droids = droidService.getAllByPilotId(pilotId = 2)
//        assertEquals(5, droids.size)
//        assertTrue(droids.all { it.manufacturer == "Serv-O-Droid" })
//    }
//
//    @Test
//    fun `createDroid inserts new droid and returns new id`() {
//        val newId = droidService.createDroid(
//            CreateDroidRequest(
//                pitCrewId = 1,
//                manufacturer = "Corellian Engineering Corp",
//                price = 7200
//            )
//        )
//
//        val inserted = droidService.getDroidById(newId)
//        assertAll(
//            { assertEquals("Corellian Engineering Corp", inserted.manufacturer) },
//            { assertEquals(7200, inserted.price) },
//            { assertEquals(1, inserted.pitCrewId) }
//        )
//    }
//
//    @Test
//    fun `updateDroidCrew changes pitCrewId`() {
//        val original = droidService.getDroidById(12)
//        assertEquals(4, original.pitCrewId)
//
//        val updatedRows = droidService.updateDroidCrew(
//            UpdateDroidRequest(
//                droidId = original.id,
//                newPitCrewId = 5
//            )
//        )
//        val updatedDroid = droidService.getDroidById(original.id)
//        assertAll(
//            { assertEquals(1, updatedRows) },
//            { assertEquals(5, updatedDroid.pitCrewId) }
//        )
//    }
//
//    @Test
//    fun `deleteDroid removes row`() {
//        val id = droidService.createDroid(
//            CreateDroidRequest(
//                pitCrewId = 4,
//                manufacturer = "Serv-O-Automata",
//                price = 3100
//            )
//        )
//        val deletedRows = droidService.deleteDroid(id)
//        assertEquals(1, deletedRows)
//
//        val exception = assertThrows<EntityNotFoundException> {
//            droidService.getDroidById(id)
//        }
//        assertEquals("DROID with id: $id not found!", exception.message)
//    }
//}