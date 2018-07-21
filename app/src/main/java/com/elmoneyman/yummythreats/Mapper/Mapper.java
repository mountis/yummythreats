package com.elmoneyman.yummythreats.Mapper;


import java.util.List;

public interface Mapper<To,From> {
    To map(From from);
    From reverseMap(To to);
    List<To> map(List<From> fromList);
}
