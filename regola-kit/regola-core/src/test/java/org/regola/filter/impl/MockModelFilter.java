package org.regola.filter.impl;

import java.util.List;

import org.regola.filter.annotation.Equals;
import org.regola.filter.annotation.GreaterThan;
import org.regola.filter.annotation.In;
import org.regola.filter.annotation.LessThan;
import org.regola.filter.annotation.Like;
import org.regola.filter.annotation.NotEquals;
import org.regola.model.ModelPattern;

/**
 * @author nicola
 */
class MockModelFilter extends ModelPattern {
	
	private static final long serialVersionUID = 1L;
	
	public MockModelFilter() {
		super(true);
	}
	
	@Equals(value = "modelName")
	String name;
	String surname;
	String nickname;
	Integer age;
	Integer weight;
	String hairColor;
	List<String> hobbies;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Equals
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Like
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@GreaterThan
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@LessThan
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@NotEquals
	public String getHairColor() {
		return hairColor;
	}

	public void setHairColor(String hairColor) {
		this.hairColor = hairColor;
	}

	@In
	public List<String> getHobbies() {
		return hobbies;
	}

	public void setHobbies(List<String> hobbies) {
		this.hobbies = hobbies;
	}
}