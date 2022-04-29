import { Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import ReactJson from "react-json-view";

export default function DestinationForm(props) {
  const { onSubmit, destination } = props;
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  return (
    <Form onSubmit={handleSubmit(onSubmit)}>
      <Form.Group className="mb-3">
        <Form.Label htmlFor="destOrg">Destination Organization</Form.Label>
        <Form.Control
          id="destOrg"
          type="text"
          isInvalid={Boolean(errors.destOrg)}
          {...register("destOrg", { required: "Destination Organization is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.destOrg?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label htmlFor="destRepo">Destination Repository</Form.Label>
        <Form.Control
          id="destRepo"
          type="text"
          isInvalid={Boolean(errors.destRepo)}
          {...register("destRepo", { required: "Destination Repository is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.destRepo?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Button type="submit" data-testid="DestinationForm-Submit-Button">Submit Destination</Button>
      <ReactJson src={destination} />
    </Form>
  );
}
