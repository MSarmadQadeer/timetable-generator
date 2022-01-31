import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        timeTable();
    }

    public static void timeTable() {
        System.out.println("___ ___ _    _  ___    ___   _    __        ___     ____   ___       ___  __    _   ___  ___   __");
        System.out.println(" |   |  |\\  /| |___     |   /_\\  |__) |    |___    |  ___ |___ |\\ | |___ |__|  /_\\   |  |   | |__|");
        System.out.println(" |  _|_ | \\/ | |___     |  /   \\ |__) |___ |___    |__| | |___ | \\| |___ | \\_ /   \\  |  |___| | \\_");
        System.out.println("__________________________________________________________________________________________________");
        System.out.println();

        ArrayList<String> rooms = new ArrayList<>();
        ArrayList<Integer> roomsCapacity = new ArrayList<>();
        ArrayList<Integer> classesCapacity = new ArrayList<>();
        ArrayList<String> classCheck = new ArrayList<>();
        //  classCheck ArrayList is created for the purpose of tracking which classes are unable to take all their lectures.
        String[] totalClasses = {"1A", "1B", "1C", "2A", "2B", "2C"};
        String[] semester1Classes = {"1A", "1B", "1C"};
        String[] semester2Classes = {"2A", "2B", "2C"};
        String[] semester1 = {"Introduction_To_Computing", "Applied_Physics", "Islamic_Studies", "Calculus", "English"};
        String[] semester2 = {"Programming_Fundamentals", "Multi_Calculus", "Discrete_Structures", "Report_Writing", "Digital_Logic_Design"};
        String[] teachersNames = {"Sir_Asif", "Mam_Nadia", "Mam_Saira", "Mam_Muneeba", "Mam_Ayesha", "Sir_Majid", "Sir_Tahir", "Mam_Shafia", "Sir_Suleiman", "Mam_Misbah"};
        String[] teachersCourses = {"Programming_Fundamentals", "Multi_Calculus", "Discrete_Structures", "Report_Writing", "Digital_Logic_Design", "Introduction_To_Computing", "Applied_Physics", "Islamic_Studies", "Calculus", "English"};

        int ask = inputInt("How Many Rooms Are Available ? ");
        for (int i = 0; i < ask; i++) {
            String ROOM = input("Enter Room : ");
            rooms.add(ROOM);
            roomsCapacity.add(inputInt("Enter " + ROOM + " Capacity: "));
        }

        //  Sort roomsCapacity and rooms
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = 0; j < (rooms.size() - 1); j++) {
                if (roomsCapacity.get(j) > roomsCapacity.get(j + 1)) {
                    int dummy = roomsCapacity.get(j);
                    roomsCapacity.set(j, roomsCapacity.get(j + 1));
                    roomsCapacity.set(j + 1, dummy);
                    String Dummy = rooms.get(j);
                    rooms.set(j, rooms.get(j + 1));
                    rooms.set(j + 1, Dummy);
                }
            }
        }

        for (String C : totalClasses) {
            classesCapacity.add(inputInt("Enter Strength Of " + C + " : "));
            classCheck.add(C);
        }

        String[][][] teachersTimeTable = new String[teachersNames.length][5][5];
        String[][][] classesTimeTable = new String[totalClasses.length][5][5];
        String[][][] roomsTimeTable = new String[rooms.size()][5][5];

        for (int teacher = 0; teacher < teachersTimeTable.length; teacher++) {
            day:
            for (int day = 0; day < 5; day++) {
                period:
                for (int period = 0; period < 5; period++) {
                    if (teachersTimeTable[teacher][day][period] == null) {
                        // ^ It Checks that the Teacher at the Particular Time Slot is Free
                        if (countNull(teachersTimeTable[teacher][day]) > 3) {
                            // ^ It Checks Teachers Day Lectures (Maximum 2 per Day)
                            String[] classes = null;
                            if (searchArray(teachersCourses[teacher], semester1)) classes = semester1Classes;
                            else if (searchArray(teachersCourses[teacher], semester2)) classes = semester2Classes;
                            // ^ Above conditions selects the specific classes related to teacher
                            for (String C : classes != null ? classes : new String[0]) {
                                if (classesTimeTable[index(C, totalClasses)][day][period] == null) {
                                    // ^ It Checks that the Class at the Particular Time Slot is Free
                                    if (countNull(classesTimeTable[index(C, totalClasses)][day]) > 2) {
                                        // ^ It Checks that the Class will take maximum 3 lecture in a Day
                                        if (count1d(C, teachersTimeTable[teacher][day]) == 0) {
                                            // ^ It Checks that the Teacher will take maximum 1 lecture in a Particular Class per Day
                                            if (count2d(C, teachersTimeTable[teacher]) < 2) {
                                                // ^ It Checks that the Teacher will take maximum 2 lecture in a Particular Class per Week
                                                for (String R : rooms) {
                                                    if (roomsTimeTable[rooms.indexOf(R)][day][period] == null) {
                                                        // ^ It Checks that the Room at the Particular Time Slot is Empty
                                                        if (roomsCapacity.get(rooms.indexOf(R)) >= classesCapacity.get(index(C, totalClasses))) {
                                                            roomsTimeTable[rooms.indexOf(R)][day][period] = C;
                                                            teachersTimeTable[teacher][day][period] = C;
                                                            classesTimeTable[index(C, totalClasses)][day][period] = teachersCourses[teacher];

                                                            if (countFill2d(classesTimeTable[index(C, totalClasses)]) == 10)
                                                                classCheck.remove(C);
                                                            // ^ It Checks the Classes that took all their Lectures

                                                            continue period;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            continue day;
                        }
                    }

                }
            }
        }

        System.out.println("_".repeat(13 + (25 * 5) + 1));
        System.out.printf("%-12s |%12S%-12S|%12S%-12S|%12S%-12S|%12S%-12S|%12S%-12S|\n", " ", "MON", "DAY", "TUES", "DAY", "WEDNES", "DAY", "THURS", "DAY", "FRI", "DAY");
        System.out.printf("%-12s |DL 1|DL 2|DL 3|DL 4|DL 5|DL 1|DL 2|DL 3|DL 4|DL 5|DL 1|DL 2|DL 3|DL 4|DL 5|DL 1|DL 2|DL 3|DL 4|DL 5|DL 1|DL 2|DL 3|DL 4|DL 5|\n", " ");
        System.out.println("-".repeat(13 + (25 * 5) + 1));
        for (int i = 0; i < teachersNames.length; i++) {
            System.out.printf("%-12s |", teachersNames[i]);
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if (teachersTimeTable[i][j][k] == null) System.out.printf("%-4s|", " ");
                    else System.out.printf("%-4s|", teachersTimeTable[i][j][k]);
                }
            }

            System.out.println();
            System.out.printf("%-12s |", " ");
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if (teachersTimeTable[i][j][k] == null) System.out.printf("%-4s|", " ");
                    else {
                        for (int r = 0; r < rooms.size(); r++) {
                            try {
                                if (roomsTimeTable[r][j][k].equals(teachersTimeTable[i][j][k]))
                                    System.out.printf("%-4s|", rooms.get(r));
                            } catch (NullPointerException ignored) {
                            }
                        }
                    }
                }
            }

            System.out.println();
        }

        System.out.println("_".repeat(13 + (25 * 5) + 1));
        for (String C : classCheck) {
            System.out.println((10 - countFill2d(classesTimeTable[index(C, totalClasses)])) + " Lectures Of " + C + " are Skipped Due To Unavailability of Rooms");
        }
    }

    public static String input(String a) {
        Scanner input = new Scanner(System.in);
        System.out.print(a);
        return input.nextLine();
    }

    public static int inputInt(String a) {
        Scanner input = new Scanner(System.in);
        for (int i = 5; i > 0; i--) {
            System.out.print(a);
            try {
                return input.nextInt();
            } catch (InputMismatchException in) {
                input.next();
                System.out.println("Invalid Data Type");
                System.out.println("You Have " + (i - 1) + " More Tries\n");
            }
        }
        System.exit(-1);
        return input.nextInt();
    }

    //    It finds the index of a value in Array
    public static int index(String a, String[] list) {
        for (int i = 0; i < list.length; i++) {
            if (a.equals(list[i])) return i;
        }
        return -1;
    }

    public static boolean searchList(String a, ArrayList<String> list) {
        for (String e : list) if (e.equals(a)) return true;
        return false;
    }

    public static boolean searchArray(String a, String[] list) {
        for (String e : list) if (e.equals(a)) return true;
        return false;
    }

    public static int countNull(String[] array) {
        int count = 0;
        for (String s : array) {
            if (s == null) count++;
        }
        return count;
    }

    public static int countFill2d(String[][] array) {
        int count = 0;
        for (String[] strings : array) {
            for (int j = 0; j < array[0].length; j++) {
                if (strings[j] != null) count++;
            }
        }
        return count;
    }

    public static int count1d(String a, String[] array) {
        int count = 0;
        for (String s : array) {
            if (a.equals(s)) count++;
        }
        return count;
    }

    public static int count2d(String a, String[][] array) {
        int count = 0;
        for (String[] strings : array) {
            for (int j = 0; j < array[0].length; j++) {
                if (a.equals(strings[j])) count++;
            }
        }
        return count;
    }
}
