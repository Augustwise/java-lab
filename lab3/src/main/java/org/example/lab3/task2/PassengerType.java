package org.example.lab3.task2;

public enum PassengerType {
    SCHOOL("Школярі"),
    STUDENT("Студенти"),
    REGULAR("Звичайні пасажири");

    private final String ukrainianName;

    PassengerType(String ukrainianName) {
        this.ukrainianName = ukrainianName;
    }

    public String getUkrainianName() {
        return ukrainianName;
    }
}
