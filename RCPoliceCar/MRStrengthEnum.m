//
//  MRStrengthEnum.m
//  RCPoliceCar
//
//  Created by Andrei Vasilescu on 21/05/15.
//  Copyright (c) 2015 mready. All rights reserved.
//

#import "MRStrengthEnum.h"

#define NONE_IMAGE @"bluetooth_signal_0"
#define VERY_WEAK_IMAGE @"bluetooth_signal_1"
#define WEAK_IMAGE @"bluetooth_signal_2"
#define MEDIUM_IMAGE @"bluetooth_signal_3"
#define GOOD_IMAGE @"bluetooth_signal_4"
#define VERY_GOOD_IMAGE @"bluetooth_signal_5"

@implementation MRStrengthEnum

+ (NSString *)imageForStrength:(MRStrength)strength {
    switch (strength) {
        case NONE:
            return NONE_IMAGE;
            
        case VERY_WEAK:
            return VERY_WEAK_IMAGE;
            
        case WEAK:
            return WEAK_IMAGE;
            
        case MEDIUM:
            return MEDIUM_IMAGE;
            
        case GOOD:
            return GOOD_IMAGE;
            
        case VERY_GOOD:
            return VERY_GOOD_IMAGE;
            
        default:
            return NONE_IMAGE;
    }
}

@end
