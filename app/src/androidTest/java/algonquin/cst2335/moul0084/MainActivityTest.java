/**

 This class contains Espresso UI tests for the MainActivity class of the login with password complexity check app.
 @Author: Chawki Moulayat
 @Version: 1.0
 */
package algonquin.cst2335.moul0084;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    /**
     * Test case to verify the behavior of the login process when the password is missing letters.
     */
    @Test
    public void mainActivityTest() {

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editText1)
        );
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                withId(R.id.button)
        );
        materialButton.perform(click());

        ViewInteraction textView = onView(
                withId(R.id.textView4));

        textView.check(matches(withText("You shall not pass")));
    }
    /**
     * Test case to verify the behavior of the login process when the password is missing an uppercase letter.
     */
    @Test
    public void testFindMissingUpperCase() {

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editText1)
        );
        // Enter a password without an uppercase letter
        appCompatEditText.perform(replaceText("password123*@$"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                withId(R.id.button)
        );
        // Click on the Login button
        materialButton.perform(click());

        ViewInteraction textView = onView(
                withId(R.id.textView4));
        // Verify that the correct message is displayed
        textView.check(matches(withText("You shall not pass")));
    }
    /**
     * Test case to verify the behavior of the login process when the password is missing a special character.
     */
    @Test
    public void testFindMissingSpecialCharacter() {

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editText1)
        );
        appCompatEditText.perform(replaceText("PASSword123"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                withId(R.id.button)
        );
        materialButton.perform(click());

        ViewInteraction textView = onView(
                withId(R.id.textView4));

        textView.check(matches(withText("You shall not pass")));
    }
    /**
     * Test case to verify the behavior of the login process when the password is missing a lowercase letter.
     */
    @Test
    public void testFindMissingLowerCase() {

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editText1)
        );
        appCompatEditText.perform(replaceText("PASSWORD123*@$"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                withId(R.id.button)
        );
        materialButton.perform(click());

        ViewInteraction textView = onView(
                withId(R.id.textView4));

        textView.check(matches(withText("You shall not pass")));
    }
    /**
     * Test case to verify the behavior of the login process when the password is missing numbers.
     */
    @Test
    public void testFindMissingNumbers() {

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editText1)
        );
        appCompatEditText.perform(replaceText("PASSWORD@$"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                withId(R.id.button)
        );
        materialButton.perform(click());

        ViewInteraction textView = onView(
                withId(R.id.textView4));

        textView.check(matches(withText("You shall not pass")));
    }
    /**
     * Test case to verify the behavior of the login process when a valid password is entered.
     */
    @Test
    public void mainValidActivityTest() {

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editText1)
        );
        appCompatEditText.perform(replaceText("PASSword123@"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                withId(R.id.button)
        );
        materialButton.perform(click());

        ViewInteraction textView = onView(
                withId(R.id.textView4));

        textView.check(matches(withText("Your password is complex enough")));
    }
}
