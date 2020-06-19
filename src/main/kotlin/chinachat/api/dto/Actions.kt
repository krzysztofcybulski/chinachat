package chinachat.api.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "action")
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageAction::class, name = "message"),
    JsonSubTypes.Type(value = WritingAction::class, name = "writing")
)
interface Action

data class MessageAction(val content: String) : Action
class WritingAction : Action
