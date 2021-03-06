package refactula.introduction_to_algorithms.insertion_sort;

import refactula.introduction_to_algorithms.IntArraySortTest;
import refactula.introduction_to_algorithms.utils.sorting.SortingAlgorithm;

public class InsertionSortTest extends IntArraySortTest {

    @Override
    protected SortingAlgorithm<Integer> sortingAlgorithm() {
        return new InsertionSort<>();
    }

}