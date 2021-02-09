package io.github.lucasstarsz.component;

/**
 * This interface groups all components under one branch, allowing for generics to work as intended.
 * <p>
 * As a side note, all classes implementing the {@code IComponent} interface should follow these conventions:
 * <ul>
 *     <li>Like C++ structs, members should <strong>always</strong> be public.</li>
 *     <li>These components are designed as Plain Old Data (POD). As such they should not contain logic, only data to be operated on.</li>
 * </ul>
 */
public interface IComponent {
}
