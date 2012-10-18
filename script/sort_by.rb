#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("sortby")

  results = RDD_SLICES.product(NUM_REDUCE_TASKS, SORTBY_NUM_TUPLES, 
    SORTBY_DISTINCT_KEYS, SORTBY_VALUE_LENGTH).map do |args|
    result = run("spark.perf.SortBy", args)
    puts result
    STDOUT.flush
    [args, result]
  end

end