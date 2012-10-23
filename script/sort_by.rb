#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("sortby")
  puts "TEST, RDD_SLICES, SORTBY_NUM_TUPLES, SORTBY_DISTINCT_KEYS, SORTBY_VALUE_LENGTH, TIME"
  results = RDD_SLICES.product(SORTBY_NUM_TUPLES, 
    SORTBY_DISTINCT_KEYS, SORTBY_VALUE_LENGTH).map do |args|
    result = run("spark.perf.SortBy", args)
    puts result
    STDOUT.flush
    [args, result]
  end

end