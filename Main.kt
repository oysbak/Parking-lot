package parking

fun main() {
    ManageParkingLot().start()
}

class ManageParkingLot {
    private var parkingLot: ParkingLot = ParkingLot(0)
    private var doExit = false
    fun start() {
        do {
            processUserInput(readLine()!!)
        } while (!doExit)
    }

    private fun processUserInput(input: String) {
        val request = input.trim().split(" ")
        if (parkingLot.size == 0 && request[0].toLowerCase() !in listOf("create", "exit")) {
            println("Sorry, a parking lot has not been created.")
        } else {
            when (request[0].toLowerCase()) {
                "create" -> createParkingLot(request[1].toInt())
                "park" -> parkCar(Car(licensePlate = request[1], color = request[2]))
                "leave" -> leave(request[1].toInt())
                "status" -> printStatus()
                "reg_by_color" -> regByColor(request[1])
                "spot_by_color" -> spotByColor(request[1])
                "spot_by_reg" -> spotByReg(request[1])
                "exit" -> doExit = true
            }
        }
    }

    private fun createParkingLot(noOfSpots: Int) {
        parkingLot = ParkingLot(noOfSpots)
        println("Created a parking lot with ${parkingLot.size} spots.")
    }

    private fun parkCar(car: Car) {
        parkingLot.parkCar(car)
    }

    private fun leave(spotNo: Int) {
        parkingLot.setSpotVacant(spotNo)
    }

    private fun regByColor(color: String) {
        val spots =
            parkingLot.spots.filter { it.isSpotOccupied() && it.car?.color.equals(color.toLowerCase(), true) }
        if (spots.isNotEmpty()) {
            println(spots.joinToString(", ") { it.car?.licensePlate.toString() })
        } else {
            println("No cars with color $color were found.")
        }
    }

    private fun spotByColor(color: String) {
        val spots =
            parkingLot.spots.filter { it.isSpotOccupied() && it.car?.color.equals(color.toLowerCase(), true) }
        if (spots.isNotEmpty()) {
            println(spots.joinToString(", ") { it.spotNo.toString() })
        } else {
            println("No cars with color $color were found.")
        }
    }

    private fun spotByReg(regNo: String) {
        val spots =
            parkingLot.spots.filter { it.isSpotOccupied() && it.car?.licensePlate.equals(regNo.toLowerCase(), true) }
        if (spots.isNotEmpty()) {
            println(spots.joinToString(", ") { it.spotNo.toString() })
        } else {
            println("No cars with registration number $regNo were found.")
        }
    }

    private fun printStatus() {
        val occupiedSpots = parkingLot.spots.filter { it.car != null }.sortedBy { it.spotNo }
        if (occupiedSpots.isEmpty()) {
            println("Parking lot is empty.")
        } else {
            for (spot in occupiedSpots) {
                println("${spot.spotNo} ${spot.car!!.licensePlate} ${spot.car!!.color}")
            }
        }
    }

    class ParkingLot(noOfSpots: Int) {
        val spots = List(noOfSpots) { i -> Spot(i + 1) }.toSet()
        val size = spots.size

        private fun getFirstVacantSpot(): Spot? = spots.firstOrNull { !it.isSpotOccupied() }

        fun parkCar(car: Car) {
            val spot = getFirstVacantSpot()
            if (spot == null) {
                println("Sorry, the parking lot is full.")
                return
            }
            spot.parkCar(car)
        }

        fun setSpotVacant(spotNo: Int) {
            spots.find { it.spotNo == spotNo }!!.setCarDeparting()
        }

        override fun toString(): String = spots.joinToString("\n")

        class Spot(val spotNo: Int) {
            var car: Car? = null
            fun isSpotOccupied() = car != null

            fun parkCar(car: Car) {
                if (isSpotOccupied()) {
                    println("Spot no. $spotNo is not vacant")
                } else {
                    this.car = car
                    println("${car.color} car parked in spot ${spotNo}.")
                }
            }

            fun setCarDeparting() {
                if (isSpotOccupied()) {
                    car = null
                    println("Spot $spotNo is free.")
                } else {
                    println("There is no car in spot $spotNo.")
                }
            }

            override fun toString() = "Spot no: $spotNo, Car: ${car ?: ""}"
        }
    }

    class Car(val licensePlate: String, val color: String) {
        override fun toString(): String {
            return "${color.capitalize()} car with reg. ${licensePlate.toUpperCase()},"
        }
    }
}

