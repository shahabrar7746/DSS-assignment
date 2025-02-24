package models;

import intefaces.Formatable;

import java.util.List;

public class Passenger implements Formatable {
        private int id;
        private String name;
        private int age;
        private String seatNumber;


        public Passenger(String name, int age) {
                this.name = name;
                this.age = age;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public int getAge() {
                return age;
        }

        public void setAge(byte age) {
                this.age = age;
        }


        public String getSeatNumber() {
                return seatNumber;
        }

        public void setSeatNumber(String seatNumber) {
                this.seatNumber = seatNumber;
        }

        @Override
        public List<String> fieldsToDisplay() {
                return List.of("name", "age");
        }

        @Override
        public List<String> getFieldValues() {
                return List.of(this.name, String.valueOf(this.age));
        }

        @Override
        public String getDisplayabletitle() {
                return "Passengers";
        }

        @Override
        public String toString() {
                return this.name;
        }
}


