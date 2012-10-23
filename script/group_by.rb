#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("groupby") 

  puts "TEST, RDD_SLICES, NUM_REDUCE_TASKS, GROUPBY_NUM_TUPLES, GROUPBY_DISTINCT_KEYS, GROUPBY_VALUE_LENGTH, TIME"
  results = RDD_SLICES.product(NUM_REDUCE_TASKS, GROUPBY_NUM_TUPLES, 
    GROUPBY_DISTINCT_KEYS, GROUPBY_VALUE_LENGTH).map do |args|
    result = run("spark.perf.GroupBy", args)
    puts result
    STDOUT.flush
    [args, result]
  end
  
end