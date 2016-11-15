package it.diamonds.tests.mocks;


import it.diamonds.engine.RandomGeneratorInterface;


public class MockRandomGenerator implements RandomGeneratorInterface
{
    private int index;

    private int numbers[];


    public MockRandomGenerator(int numbers[])
    {
        index = -1;
        setNumbers(numbers);
    }


    public MockRandomGenerator()
    {
        index = -1;

        int dummy[] = { 0, 1, 2, 3 };
        this.numbers = dummy;
    }


    public void setNumbers(int numbers[])
    {
        this.numbers = numbers;
    }


    public int extract(int module)
    {
        index = (index + 1) % numbers.length;
        return numbers[index] % module;
    }


    public long getSeed()
    {
        return 0;
    }


    public MockRandomGenerator clone()
    {
        return new MockRandomGenerator(numbers);
    }
}
