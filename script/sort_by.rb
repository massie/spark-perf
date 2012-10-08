#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"

if TESTS.include?("sortby")

  results = RDD_SLICES.product(NUM_REDUCE_TASKS, SORTBY_NUM_TUPLES, 
    SORTBY_DISTINCT_KEYS, SORTBY_VALUE_LENGTH).map do |args|
    result = `#{File.dirname(__FILE__) + "/run.rb spark.perf.SortBy " + args.join(" ")}`
    puts "SortBy,#{args.join(",")},#{result}"
    [args, result]
  end

end