package com.Razxoxo5.Toqsam.domain.usecase

import com.Razxoxo5.Toqsam.domain.model.PlayerProgress

class ClaimChallengeUseCase {

    operator fun invoke(progress: PlayerProgress, date: String): PlayerProgress =
        progress.copy(claimedChallengeDate = date)
}
